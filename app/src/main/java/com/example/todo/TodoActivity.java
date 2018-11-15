package com.example.todo;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TodoActivity extends AppCompatActivity {

    private static final int IS_SUCCESS = 0;
    private String[] mTodos;
    private int mTodoIndex = 0;
    private static final String TODO_INDEX = "todoIndex";

    public static final String TAG = "todoAtivity";

    private static final String IS_TODO_COMPLETE = "com.example.isTodoComplete";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(TODO_INDEX, mTodoIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == IS_SUCCESS ){
            if (intent != null) {
                // data in intent from child activity
                boolean isTodoComplete = intent.getBooleanExtra(IS_TODO_COMPLETE, false);
                updateTodoComplete(isTodoComplete);
            } else {
                Toast.makeText(this, R.string.back_button_pressed, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.request_code_mismatch,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateTodoComplete(boolean is_todo_complete) {

        final TextView textViewTodo;
        textViewTodo = (TextView) findViewById(R.id.textViewTodo);

        if (is_todo_complete) {
            textViewTodo.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.backgroundSuccess));
            textViewTodo.setTextColor(
                    ContextCompat.getColor(this, R.color.colorSuccess));

            setTextViewComplete("\u2713");
        }

    }

    private void setTextViewComplete( String message ){
        final TextView textViewComplete;
        textViewComplete = (TextView) findViewById(R.id.textViewComplete);

        textViewComplete.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);
        Log.d(TAG, "*** Just to say the PC is in onCreate!");

        // set the user interface layout for this Activity
        // the layout file is defined in the project res/layout/activity_todo.xml file
        setContentView(R.layout.activity_todo);

        if (savedInstanceState != null){
            mTodoIndex = savedInstanceState.getInt(TODO_INDEX, 0);
        }
        // initialize member TextView so we can manipulate it later
        final TextView TodoTextView;
        TodoTextView = findViewById(R.id.textViewTodo);

        // read the todo array from res/values/strings.xml
        Resources res = getResources();
        mTodos = res.getStringArray(R.array.todo);
        // display the first task from mTodo array in the TodoTextView
        TodoTextView.setText(mTodos[mTodoIndex]);

        Button buttonNext;
        buttonNext = findViewById(R.id.buttonNext);

        Button buttonPrev;
        buttonPrev = findViewById(R.id.buttonPrev);

        Button buttonDetail;
        buttonDetail = findViewById(R.id.buttonTodoDetail);

        // OnClick listener for the  Next button
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTodoIndex = (mTodoIndex + 1) % mTodos.length;
                TodoTextView.setText(mTodos[mTodoIndex]);
            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTodoIndex = (mTodoIndex + mTodos.length - 1) % mTodos.length;
                TodoTextView.setText(mTodos[mTodoIndex]);
            }
        });
        buttonDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = TodoDetailActivity.newIntent(TodoActivity.this, mTodoIndex);
                startActivityForResult(intent, IS_SUCCESS);
            }
        });
    }
}
