package com.app.market.api.events;

/**
 * Created by OmarAli on 26/01/2016.
 *
 * this class used to hold query parameters that used to form the requested url
 */
public class GetProductEvent {
    private String _UUID;
    private String _minorNumber;
    private String _majorNumber;

    public GetProductEvent(String _UUID, String _majorNumber ,String _minorNumber){
        this._UUID = _UUID;
        this._minorNumber = _minorNumber;
        this._majorNumber = _majorNumber;
    }

    public String get_UUID() {
        return _UUID;
    }

    public String get_minorNumber() {
        return _minorNumber;
    }

    public String get_majorNumber() {
        return _majorNumber;
    }
}
