package com.codepath.quest;

import android.app.Application;

import com.codepath.quest.model.Answer;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Question;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See https://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Subject.class);
        ParseObject.registerSubclass(Section.class);
        ParseObject.registerSubclass(Page.class);
        ParseObject.registerSubclass(Question.class);
        ParseObject.registerSubclass(Answer.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("IuO7g88uTAhhdb0KVD6j2jyYJvCXSuhPAKQ44YOY")
                .clientKey("4FdGsourn3aBjSZFvvJ9of95f1MSCcjPF06Bf0I9")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
