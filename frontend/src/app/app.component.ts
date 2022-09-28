import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import {VehicleLocation, WebsocketService} from "./common/websocket.service";
import {} from 'googlemaps';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [WebsocketService]
})
export class AppComponent implements OnInit, OnDestroy {

  @ViewChild('gmapContainer', {static: false}) gmap: ElementRef;

  title = 'frontend';
  received: VehicleLocation[] = [];
  map: google.maps.Map;

  private subscription: Subscription;
  private markers: google.maps.Marker[] = [];

  constructor(private websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.subscription = this.websocketService.messages.subscribe(message => {
      this.received.push(message);

      let newestLocation = this.received
        .filter(receivedMessage => receivedMessage.vin === message.vin)
        .reduce((a, b) =>  a.timestamp > b.timestamp ? a : b);

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
    });
  }

  ngAfterViewInit() {
    let mapOptions: google.maps.MapOptions = {
      center:  new google.maps.LatLng(48.84473, 8.54694),
      zoom: 16,
      minZoom: 15,
      maxZoom: 18
    };
    this.map = new google.maps.Map(this.gmap.nativeElement, mapOptions);

    this.map.addListener('zoom_changed', () => {
      this.markers.forEach(marker => this.setMarkerIconBasedOnZoom(marker));
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private setMarkerIconBasedOnZoom(marker: google.maps.Marker): void {
    let zoom = this.map.getZoom();
    marker.setIcon({
      url:"assets/car-icon.png",
      scaledSize: new google.maps.Size(20 * Math.sqrt(zoom / 18),42 * Math.sqrt( zoom / 18)),
      anchor: new google.maps.Point(10,21)
    });
  }
}
