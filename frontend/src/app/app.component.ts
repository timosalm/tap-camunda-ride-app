import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { WebsocketService } from "./common/websocket.service";
import {} from 'googlemaps';
import {BusinessEvent} from "./common/business-event.entity";
import {VehicleLocation} from "./vehicle-location.entity";
import {RideRequest} from "./ride-request.entity";
import {Location} from "./location.entity";
import * as uuid from 'uuid';
import {NotifyDriverData} from "./notify-driver-data.entity";
import {RideAcceptance} from "./ride-acceptance.entity";
import {NotifyMatchData} from "./notify-match-data.entity";
import {Rider} from "./rider.entity";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [WebsocketService]
})
export class AppComponent implements OnInit, OnDestroy {

  @ViewChild('gmapContainer', {static: false}) gmap: ElementRef;
  @ViewChild('gmapSearchField', {static: false}) gmapSearchField: ElementRef;
  userId = "af659827-db47-4111-a255-8a3516fa70a4";
  showMap = true;
  riderNotifyMatchData?: NotifyMatchData
  pickedUp = false;
  rideFinished = false;

  driver = '1HMD11338H4E954D9';
  driverRequestNotifications: NotifyDriverData[] = [];
  driverNotifyMatchData?: NotifyMatchData
  riderPickedUp = false;

  private subscription: Subscription;
  private map: google.maps.Map;
  private searchBox: google.maps.places.SearchBox;
  private markers: google.maps.Marker[] = [];
  private receivedEvents: BusinessEvent[] = [];
  private userLocation: Location;
  private destinationMarker: google.maps.Marker;
  constructor(private websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.subscription = this.websocketService.events.subscribe(event => {
      this.receivedEvents.push(event);
      this.handleBusinessEvent(event);
    });
  }

  ngAfterViewInit() {
    let mapOptions: google.maps.MapOptions = {
      zoom: 16,
      streetViewControl: false,
    };
    this.map = new google.maps.Map(this.gmap.nativeElement, mapOptions);

    this.showCurrentUserLocation();

    this.map.addListener('zoom_changed', () => {
      this.markers.forEach(marker => this.setMarkerIconBasedOnZoom(marker));
    });

    this.searchBox = new google.maps.places.SearchBox(this.gmapSearchField.nativeElement);
    this.map.controls[google.maps.ControlPosition.TOP_CENTER].push(this.gmapSearchField.nativeElement);

    this.searchBox.addListener('places_changed', () => {
      let placesLocation = this.searchBox.getPlaces()[0].geometry?.location;
      if (!placesLocation) return;

      let rideRequest = new RideRequest(this.userId, this.userLocation, new Location(placesLocation.lat(), placesLocation.lng()));
      this.websocketService.events.next(new BusinessEvent(uuid.v4(), BusinessEvent.RIDE_REQUESTED, rideRequest));
      if (this.destinationMarker) {
        this.destinationMarker.setPosition(placesLocation);
      } else {
        this.destinationMarker = this.createCircleMarker(placesLocation, "#F00");
      }
      let bounds = new google.maps.LatLngBounds();
      bounds.extend(new google.maps.LatLng(this.userLocation.latitude, this.userLocation.longitude));
      bounds.extend(placesLocation);
      this.map.fitBounds(bounds);
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  acceptRide(userId: string) {
    this.showMap = false;
    let rideAcceptance = new RideAcceptance(userId, this.driver);
    this.websocketService.events.next(new BusinessEvent(uuid.v4(), BusinessEvent.DRIVER_ACCEPTED, rideAcceptance));
    this.driverRequestNotifications = this.driverRequestNotifications.filter(obj => obj.userId !== userId);
  }

  pickUpRider(userId: string) {
    this.websocketService.events.next(new BusinessEvent(uuid.v4(), BusinessEvent.RIDER_PICKED_UP, new Rider(userId)));
    this.pickedUp = true;
    this.riderPickedUp = true;
  }

  finishRide(userId: string) {
    this.websocketService.events.next(new BusinessEvent(uuid.v4(), BusinessEvent.RIDE_FINISHED, new Rider(userId)));
    this.driverNotifyMatchData = undefined;
    this.rideFinished = true;
    this.riderPickedUp = false;
  }

  resetUI() {
    this.rideFinished = false;
    this.pickedUp = false;
    this.riderNotifyMatchData = undefined;
    this.showMap = true;
  }

  private handleBusinessEvent(event: BusinessEvent) {
    if (event.type === BusinessEvent.VEHICLE_LOCATION_CHANGED) {
      let location = event.data as VehicleLocation
      let newestLocation = this.receivedEvents
        .filter(receivedEvent => receivedEvent.type === BusinessEvent.VEHICLE_LOCATION_CHANGED)
        .map(receivedEvent => receivedEvent.data as VehicleLocation)
        .filter(receivedLocation => receivedLocation.vin === location.vin)
        .reduce((a, b) => a.timestamp > b.timestamp ? a : b);

      let marker = this.markers.find(marker => marker.get('vin') === newestLocation.vin);
      if (marker) {
        marker.setPosition(new google.maps.LatLng(newestLocation.latitude, newestLocation.longitude));
      } else {
        let newMarker = new google.maps.Marker({
          position: new google.maps.LatLng(newestLocation.latitude, newestLocation.longitude),
          map: this.map,
        });
        newMarker.set('vin', newestLocation.vin);
        this.setMarkerIconBasedOnZoom(newMarker);
        this.markers.push(newMarker);
      }
    } else if (event.type === BusinessEvent.NOTIFY_DRIVER) {
      let driverRequestNotification = event.data as NotifyDriverData;
      if (driverRequestNotification.driver === this.driver) {
        this.driverRequestNotifications.push(driverRequestNotification);
      }
    } else if (event.type === BusinessEvent.MATCH_CONFIRMED) {
      let notifyMatchData = event.data as NotifyMatchData;
      if (notifyMatchData.userId === this.userId) {
        this.riderNotifyMatchData = notifyMatchData;
      }
      if (notifyMatchData.driver === this.driver) {
        this.driverNotifyMatchData = notifyMatchData;
      }
    }
  }

  private setMarkerIconBasedOnZoom(marker: google.maps.Marker): void {
    let zoom = this.map.getZoom();
    marker.setIcon({
      url:"assets/car-icon.png",
      scaledSize: new google.maps.Size(20 * Math.sqrt(zoom / 18),42 * Math.sqrt( zoom / 18)),
      anchor: new google.maps.Point(10,21)
    });
  }

  private showCurrentUserLocation() {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        let googleCoords = position.coords;
        this.userLocation = new Location(googleCoords.latitude, googleCoords.longitude);

        let googleUserLocation = new google.maps.LatLng(googleCoords.latitude, googleCoords.longitude);
        this.map.setCenter(googleUserLocation);
        this.createCircleMarker(googleUserLocation, "#000");
      });
    }
  }

  private createCircleMarker(location: google.maps.LatLng, color: string) {
    return new google.maps.Marker({
      position: location,
      icon: {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 8,
        strokeColor: color,
      },
      map: this.map
    });
  }

}
