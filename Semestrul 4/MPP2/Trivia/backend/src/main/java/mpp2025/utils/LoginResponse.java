package mpp2025.utils;

public abstract class LoginResponse extends ResponseBase {
    public static class WrongUsername extends LoginResponse {}

    public static class Success extends LoginResponse {}
}
