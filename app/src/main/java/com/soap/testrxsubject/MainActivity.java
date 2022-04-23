package com.soap.testrxsubject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class MainActivity extends AppCompatActivity {
    private Button timerButton;
    private Button sub1Button;
    private Button sub2Button;
    private TextView sub1TextView;
    private TextView sub2TextView;
    private Disposable timerDispose;
    private Disposable sub1Task;
    private Disposable sub2Task;
    private final Subject<String> subject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.timerButton = findViewById(R.id.timer_button);
        this.sub1Button = findViewById(R.id.sub1_button);
        this.sub2Button = findViewById(R.id.sub2_button);
        this.sub1TextView = findViewById(R.id.sub1_tv);
        this.sub2TextView = findViewById(R.id.sub2_tv);

        timerButton.setOnClickListener( v -> {
            if( timerDispose != null ){
                timerDispose.dispose();
                timerDispose = null;
                timerButton.setText( R.string.start );
            }
            else {
                timerDispose = Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe(t -> subject.onNext(String.format("broadcast:%s", t)));
                timerButton.setText( R.string.end );
            }
        } );

        sub1Button.setOnClickListener( v -> {
            if( sub1Task != null ) {
                if( !sub1Task.isDisposed() ) {
                    sub1Task.dispose();
                }
                sub1Task = null;
                sub1Button.setText( R.string.start );
            }
            else{
                sub1Task = subject.observeOn(AndroidSchedulers.mainThread())
                        .subscribe( t-> this.sub1TextView.setText( t ) );
                sub1Button.setText( R.string.end );
            }
        } );

        sub2Button.setOnClickListener(v->{
            if( sub2Task != null ) {
                if( !sub2Task.isDisposed() ) {
                    sub2Task.dispose();
                }
                sub2Task = null;
                sub2Button.setText( R.string.start );
            }
            else{
                sub2Task = subject.observeOn(AndroidSchedulers.mainThread())
                        .subscribe( t-> this.sub2TextView.setText( t ) );
                sub2Button.setText( R.string.end );
            }
        });
    }
}