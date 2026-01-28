import {Box, CardMedia} from "@mui/material";
import {ImageNotSupported} from "@mui/icons-material";

interface ImageMediaProps {
    displayImage: boolean,
    imageUrl: string,
    altText?: string,
    height?: number | string,
    width?: number | string,
    maxHeight?: number | string,
}

export default function ImageMedia({ displayImage, imageUrl, altText, height, maxHeight, width }: ImageMediaProps) {
    if (displayImage) {
        return (
            <CardMedia
                component="img"
                height={height}
                width={width}
                image={imageUrl}
                alt={altText}
                sx={{
                    aspectRatio: "1 / 1",
                    maxHeight: {maxHeight},
                    objectFit: "contain",
                    backgroundColor: "transparent"
                }}
            />
        )
    }

    return (
        <Box
            sx={{
                height: {height},
                width: {width},
                maxHeight: {maxHeight},
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                bgcolor: "grey.100",
                aspectRatio: "1 / 1"
            }}
        >
            <ImageNotSupported sx={{ fontSize: 80, color: "grey.400" }} />
        </Box>
    )
}