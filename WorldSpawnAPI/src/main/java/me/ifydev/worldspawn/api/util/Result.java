package me.ifydev.worldspawn.api.util;

import lombok.AllArgsConstructor;

import java.util.Optional;

/**
 * @author Innectic
 * @since 08/06/2018
 */
@AllArgsConstructor
public class Result<Success, Error> {

    public interface ResultRunnable<Success> {
        void run(Success value);
    }

    private Success success;
    private Error error;

    public boolean ok() {
        return error == null && success != null;
    }

    public boolean err() {
        return error != null && success == null;
    }

    public Optional<Success> getOk() {
        return Optional.ofNullable(success);
    }

    public Optional<Error> getErr() {
        return Optional.ofNullable(error);
    }

    public void ifFilled(ResultRunnable<Success> runnable) {
        if (!ok()) return;

        runnable.run(success);
    }

    public static<Success, Error> Result<Success, Error> fromSuccess(Success sucess) {
        return new Result<>(sucess, null);
    }

    public static<Success, Error> Result<Success, Error> fromErr(Error error) {
        return new Result<>(null, error);
    }
}