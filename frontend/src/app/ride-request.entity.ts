import {Location} from "./location.entity";

export class RideRequest {

  constructor(public userId: string, public from: Location, public to: Location) {}
}
