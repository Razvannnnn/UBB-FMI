package mpp2025.utils;

import lombok.Getter;

public class SubmitGameResponse extends ResponseBase {
    @Getter
    public static class Success extends SubmitGameResponse
    { }
}
