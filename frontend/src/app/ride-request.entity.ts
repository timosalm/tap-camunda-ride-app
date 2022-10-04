import {Location} from "./location.entity";

export class RideRequest {

  constructor(public from: Location, public to: Location) {}
}
