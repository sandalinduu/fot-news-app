package lk.cmb.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> eventsNewsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventsNewsList = new ArrayList<>();
        adapter = new NewsAdapter(eventsNewsList);
        eventsRecyclerView.setAdapter(adapter);

        fetchEventsNewsFromFirebase();
        return view;
    }

    private void fetchEventsNewsFromFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news/events");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsNewsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    NewsItem newsItem = postSnapshot.getValue(NewsItem.class);
                    if (newsItem != null) {
                        eventsNewsList.add(newsItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error loading events news: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
