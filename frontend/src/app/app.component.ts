import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { WebsocketService } from "./common/websocket.service";
import {} from 'googlemaps';
import {BusinessEvent} from "./common/business-event.entity";
import {VehicleLocation} from "./vehicle-location.entity";
import {RideRequest} from "./ride-request.entity";
import {Location} from "./location.entity";
import * as uuid from 'uuid';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [WebsocketService]
})
export class AppComponent implements OnInit, OnDestroy {

  @ViewChild('gmapContainer', {static: false}) gmap: ElementRef;
  @ViewChild('gmapSearchField', {static: false}) gmapSearchField: ElementRef;

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
      minZoom: 15,
      maxZoom: 18,
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

      let rideRequest = new RideRequest(this.userLocation, new Location(placesLocation.lat(), placesLocation.lng()));
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
