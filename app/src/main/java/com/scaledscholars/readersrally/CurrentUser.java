package com.scaledscholars.readersrally;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {
    public static String id() {
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        return u == null ? "" : u.getUid();
    }
}
