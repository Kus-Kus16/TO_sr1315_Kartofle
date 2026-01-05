export interface BoardGameTypeUpdate {
    description?: string;
    minutesPlaytime: number;
}

export interface BoardGameTypeCreate extends BoardGameTypeUpdate {
    title: string;
    minPlayers: number;
    maxPlayers: number;
}

export interface BoardGameTypeFull extends BoardGameTypeCreate {
    id: number;
    discontinued: boolean;
    imageUrl?: string;
    rulebookUrl?: string;
}

