import { Injectable } from "@angular/core";
import { Observable, Observer } from 'rxjs';
import { AnonymousSubject } from 'rxjs/internal/Subject';
import { Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import {environment} from "../../environments/environment";
import {BusinessEvent} from "./business-event.entity";

@Injectable()
export class WebsocketService {
  private subject: AnonymousSubject<MessageEvent>;
  public events: Subject<BusinessEvent>;

  constructor() {
    this.events = <Subject<BusinessEvent>>this.connect(environment.websocket_url).pipe(
      map(
        (response: MessageEvent): BusinessEvent => {
          console.log(response.data);
          return JSON.parse(response.data);
        }
      )
    );
  }

  public connect(url: string): AnonymousSubject<MessageEvent> {
    if (!this.subject) {
      this.subject = this.create(url);
      console.log("Successfully connected: " + url);
    }
    return this.subject;
  }

  private create(url: string): AnonymousSubject<MessageEvent> {
    let ws = new WebSocket(url);
    let observable = new Observable((obs: Observer<MessageEvent>) => {
      ws.onmessage = obs.next.bind(obs);
      ws.onerror = obs.error.bind(obs);
      ws.onclose = obs.complete.bind(obs);
      return ws.close.bind(ws);
    });
    let observer = {
      error: () => {},
      complete: () => {
        if (ws.readyState == WebSocket.OPEN) ws.close();
      },
      next: (data: Object) => {
        console.log('Message sent to websocket: ', data);
        if (ws.readyState === WebSocket.OPEN) {
          ws.send(JSON.stringify(data));
        }
      }
    };
    return new AnonymousSubject<MessageEvent>(observer, observable);
  }
}
