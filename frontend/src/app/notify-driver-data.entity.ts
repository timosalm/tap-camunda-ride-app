import {Location} from "./location.entity";

export class NotifyDriverData {

  constructor(public userId: string, public destination: Location, public startingPoint: Location, public driver: string) {}
}
