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

public class SportFragment extends Fragment {

    private RecyclerView sportRecyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> sportNewsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport, container, false);

        sportRecyclerView = view.findViewById(R.id.sportRecyclerView);
        sportRecyclerView.setHasFixedSize(true);
        sportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sportNewsList = new ArrayList<>();
        adapter = new NewsAdapter(sportNewsList);
        sportRecyclerView.setAdapter(adapter);

        fetchSportNewsFromFirebase();
        return view;
    }

    private void fetchSportNewsFromFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news/sport");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sportNewsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    NewsItem newsItem = postSnapshot.getValue(NewsItem.class);
                    if (newsItem != null) {
                        sportNewsList.add(newsItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error loading sports news: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
