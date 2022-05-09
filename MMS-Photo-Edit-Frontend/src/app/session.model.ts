export interface Session {
  id: string;
  created: Date;
  updated: Date | null;
  expires: Date;
}
