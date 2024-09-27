package com.dpilaloa.upsipteeo.data.interfaces;

import com.google.firebase.database.DatabaseError;

public interface DbErrorInterface {

    void onProcessError(DatabaseError databaseError);

}
