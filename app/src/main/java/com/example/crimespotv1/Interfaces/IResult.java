package com.example.crimespotv1.Interfaces;

import com.android.volley.VolleyError;

public interface IResult {
    public void notifySuccess(String ID, String response);
    public void notifyError(String ID, VolleyError error);
}
