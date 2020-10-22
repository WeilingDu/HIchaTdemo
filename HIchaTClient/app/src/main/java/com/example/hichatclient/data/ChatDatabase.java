package com.example.hichatclient.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// singleton
@Database(entities = {User.class, Friend.class, ChattingContent.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase {
    private static ChatDatabase INSTANCE;
    public static synchronized ChatDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ChatDatabase.class, "chat_database")
                    .build();
        }
        return INSTANCE;
    }

    public abstract UserDao getUserDao();

    public abstract FriendDao getFriendDao();
    public abstract ChattingContentDao getChattingContentDao();
}
