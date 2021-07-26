package shop.dropapp.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import shop.dropapp.utils.Status;
import shop.dropapp.utils.Urls;

public class ApiResponse {
    public final Status status;
    @Nullable
    public final String data;
    @Nullable
    public final String error;

    private ApiResponse(Status status, @Nullable String data, @Nullable String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(Status.LOADING, null, null);
    }

    public static ApiResponse success(@NonNull String data) {
        return new ApiResponse(Status.SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull String error) {
        Log.e(Urls.TAG, "error: "+error);

        return new ApiResponse(Status.ERROR, null, error);
    }
}
