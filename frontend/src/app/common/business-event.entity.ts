export class BusinessEvent {

  public static readonly VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";
  public static readonly RIDE_REQUESTED = "ride-requested";
  public static readonly NOTIFY_DRIVER = "notify-driver";
  public static readonly MATCH_CONFIRMED = "match-confirmed";
  public static readonly DRIVER_ACCEPTED = "driver-accepted";
  public static readonly RIDER_PICKED_UP = "rider-picked-up";
  public static readonly RIDE_FINISHED = "ride-finished";

  constructor(public id: string, public type: string, public data: any) {}
}
