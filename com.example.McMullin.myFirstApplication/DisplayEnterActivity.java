/**
 *
 * Created by Ciara McMullin
 * A class that diplays the food entries and saves them into the database
 *
 **/
package com.example.McMullin.myfirstapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayEnterActivity extends AppCompatActivity {

    NoteAdapter adapter=null;
    NoteHelper helper=null;
    Cursor dataset_cursor=null;
    EditText editNote=null;
    String noteId=null;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try
            {
                setContentView(R.layout.activity_display_enter);

                ListView list= (ListView)findViewById(R.id.list);
                editNote = (EditText)findViewById(R.id.myEditText);
                //get our helper
                helper=new NoteHelper(this);

                //get our cursor
                dataset_cursor=helper.getAll();
                //manage the cursor
                startManagingCursor(dataset_cursor);
                //pass it to our adapter
                adapter=new NoteAdapter(dataset_cursor);
                //set the adapter on our list
                list.setAdapter(adapter);

                //button
                Button btnSimple = (Button) findViewById(R.id.entry);
                //listen for the click
                btnSimple.setOnClickListener(onSave);

                //Delete button
                Button btnDelete = (Button) findViewById(R.id.btnDelete);
                //listen for the click
                btnDelete.setOnClickListener(onDelete);

                //this is how we know what to do when a list item is clicked
                list.setOnItemClickListener(onListClick);


            }
            catch (Exception e)
            {
                // error message to the log
                Log.e("ERROR", "ERROR IN CODE: " + e.toString());

                // prints out the location of the code where the error occurred.
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            helper.close();
        }
        private View.OnClickListener onSave=new View.OnClickListener() {
            public void onClick(View v) {
                //add the note if new
                if (noteId==null) {
                    helper.insert(editNote.getText().toString());
                }
                else{
                    helper.update(noteId, editNote.getText().toString());
                    //set noteId back to null, so the next note will be added as new
                    noteId=null;

                }
                dataset_cursor.requery();
                //update the view
                //erase the text so we can add another note
                editNote.setText("");
            }
        };

        private View.OnClickListener onDelete=new View.OnClickListener() {
            public void onClick(View v) {
                //if we haven't clicked a note yet, do nothing
                if (noteId==null) {
                    return;
                }
                else{
                    //we have have clicked a note, then delete it!
                    helper.delete(noteId);
                    //set noteId back to null, so the next note will be added as new
                    noteId=null;

                }
                dataset_cursor.requery();
                //update the view
                //erase the text so we can add another note
                editNote.setText("");
            }
        };

        private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position,
                                    long id)
            {
                //get the id of the item in the list clicked on
                //we need this so we can update the changes later
                noteId =String.valueOf(id);
                //query the database for that ID
                Cursor c=helper.getById(noteId);

                //move the cursor to first position, just in case
                c.moveToFirst();
                //get the text of the note, set our EditText to this
                editNote.setText(helper.getNote(c));

            }
        };

        class NoteAdapter extends CursorAdapter {
            NoteAdapter(Cursor c) {
                super(DisplayEnterActivity.this, c);
            }

            @Override
            public void bindView(View row, Context ctxt,
                                 Cursor c) {
                NoteHolder holder=(NoteHolder)row.getTag();

                holder.populateFrom(c, helper);
            }

            @Override
            public View newView(Context ctxt, Cursor c,
                                ViewGroup parent) {
                LayoutInflater inflater=getLayoutInflater();
                View row=inflater.inflate(R.layout.row, parent, false);
                NoteHolder holder=new NoteHolder(row);

                row.setTag(holder);

                return(row);
            }
        }

        static class NoteHolder {
            private TextView noteText=null;

            NoteHolder(View row) {
                noteText=(TextView)row.findViewById(R.id.note);
            }

            void populateFrom(Cursor c, NoteHelper helper) {
                noteText.setText(helper.getNote(c));

            }
        }
    }
