export class BusinessEvent {

  public static readonly VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";
  public static readonly RIDE_REQUESTED = "ride-requested";

  constructor(public id: string, public type: string, public data: any) {}
}
