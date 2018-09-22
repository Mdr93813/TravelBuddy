package android.com.avishkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by UTSAV JAIN on 9/16/2018.
 */

public class MyTripFragment extends Fragment {

    private String titlefetch,sourcefetch,destfetch;
    private Date datefetch;
    private ArrayList<CurrentTour>tourslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.trip_fragment,container,false);
        final ListView triplistview = view.findViewById(R.id.trip_list);
        final String reducedemail=Dashboard.signinemail;
        final FloatingActionButton saveCurrentTour=(FloatingActionButton)view.findViewById(R.id.saveCurrentTour);
        saveCurrentTour.setVisibility(view.INVISIBLE);
        saveCurrentTour.setClickable(false);


        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference().child("users").child(reducedemail).child("ongoingTrip").child("current");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentTour curr=dataSnapshot.getValue(CurrentTour.class);
                if(curr==null){
                    saveCurrentTour.setVisibility(view.VISIBLE);
                    saveCurrentTour.setClickable(true);
                    saveCurrentTour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent totemp=new Intent(getActivity(),Temp.class);
                            Toast.makeText(getActivity(), reducedemail, Toast.LENGTH_SHORT).show();
                            totemp.putExtra("email",reducedemail);
                            startActivity(totemp);
                        }
                    });
                }
                else{
//                    titlefetch=curr.title;
//                    sourcefetch=curr.source;
//                    destfetch=curr.destination;
//                    datefetch=curr.startDate;
//                    Toast.makeText(getActivity(),titlefetch+"\n"+sourcefetch+"\n"+destfetch+"\n"+datefetch,Toast.LENGTH_LONG).show();
                    tourslist=new ArrayList<>();
                    tourslist.add(curr);
                    TripListAdapter triplist=new TripListAdapter(getActivity(),R.layout.trip_list_item,tourslist);
                    triplistview.setAdapter(triplist);
                }
            }

            //            DatabaseReference pastref=database.getReference().child("users").child(reducedemail).child("ongoingTrip");
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError+"",Toast.LENGTH_LONG).show();
            }
        });
        triplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CurrentTour tour=(CurrentTour) triplistview.getItemAtPosition(i);
                Toast.makeText(getActivity(),tour.title,Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), reducedemail, Toast.LENGTH_SHORT).show();
                Intent topresenttrip=new Intent(getContext(),PresentTrip.class);
                topresenttrip.putExtra("topresenttrip",tour);
                topresenttrip.putExtra("email",reducedemail);
                startActivity(topresenttrip);
                getActivity().finish();
            }
        });

        return view;
    }
}