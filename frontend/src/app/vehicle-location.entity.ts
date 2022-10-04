import {Location} from "./location.entity";

export interface VehicleLocation extends Location {
  vin: string;
  timestamp: Date;
}
