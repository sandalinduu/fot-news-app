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

public class AcademicFragment extends Fragment {

    private RecyclerView academicRecyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> academicNewsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic, container, false);

        academicRecyclerView = view.findViewById(R.id.academicRecyclerView);
        academicRecyclerView.setHasFixedSize(true);
        academicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        academicNewsList = new ArrayList<>();
        adapter = new NewsAdapter(academicNewsList);
        academicRecyclerView.setAdapter(adapter);

        fetchAcademicNewsFromFirebase();
        return view;
    }

    private void fetchAcademicNewsFromFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news/academic");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                academicNewsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    NewsItem newsItem = postSnapshot.getValue(NewsItem.class);
                    if (newsItem != null) {
                        academicNewsList.add(newsItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error loading academic news: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
