import type { BoardGameType } from "./boardgame";
import type {User} from "./user.ts";

export interface GameSessionType {
  id: number;
  date: string;
  numberOfPlayers: number;
  description: string;
  boardGame: BoardGameType;
  owner: User;
  participants: User[];
}