/* tslint:disable */
/* eslint-disable */

// Generated using typescript-generator version 2.30.840 on 2022-06-23 18:17:18.

export interface Registration {
  id?: number;
  start?: Date;
  end?: Date;
  username?: string;
  title?: string;
  text?: string;
}

export interface ConflictCheckResponse {
  conflicts?: Registration[];
  registration?: Registration;
  shifted?: boolean;
}
