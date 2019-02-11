package de.consol.labs.microprofilearticle.prophecy.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Type of prophecy.")
public enum ProphecyType {
    @Schema(
            description = "BEAR means the stock is expected to fall." +
                    " In this case the prophecy will be fulfilled" +
                    " when the actual future stock value does not exceed the given one."
    )
    BEAR,

    @Schema(
            description = "BULL means the stock is expected to grow." +
                    " In this case the prophecy will be fulfilled" +
                    " when the actual future stock value is at least as the given one."
    )
    BULL
}
