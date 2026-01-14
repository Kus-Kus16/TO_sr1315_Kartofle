import type { BoardGameTypeDetails } from "./BoardGameType.ts";
import type {UserType} from "./UserType.ts";
import type {VoteTypeDetails} from "./VoteType.ts";

interface GameSessionTypeBasic {
    title: string;
    date: string;
    numberOfPlayers: number;
    description: string;
}

export interface GameSessionTypeCreate extends GameSessionTypeBasic {
    boardGamesIds: number[];
}

export interface GameSessionTypePreview extends GameSessionTypeBasic {
    id: number;
    ownerId: number;
    participants: UserType[];
    selectedBoardGame: BoardGameTypeDetails | undefined;
}

export interface GameSessionTypeDetails extends GameSessionTypeBasic {
    id: number;
    ownerId: number;
    participants: UserType[];
    selectedBoardGameId?: number;
    boardGames: BoardGameTypeDetails[];
    votes: VoteTypeDetails[];
}