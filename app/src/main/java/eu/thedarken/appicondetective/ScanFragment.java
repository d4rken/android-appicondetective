package eu.thedarken.appicondetective;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class ScanFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ScanItem>> {
    private static final int LOADER_ID = 1;
    private ListView mListView;
    private ScanListAdapter mAdapter;
    private TextView mInfo;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        mInfo = (TextView) layout.findViewById(R.id.tv_info);
        mListView = (ListView) layout.findViewById(R.id.lv_result);
        mProgressBar = (ProgressBar) layout.findViewById(android.R.id.empty);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListView.setEmptyView(mProgressBar);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Drawable defaultIcon = getActivity().getApplicationInfo().loadIcon(getActivity().getPackageManager());
        Bitmap bitmap = ((BitmapDrawable) defaultIcon).getBitmap();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mInfo.setText("Optimal size: " + w + "W " + h + "H (" + (h * w) + ")" + "\n display density: " + metrics.density);
        mAdapter = new ScanListAdapter(w, h);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scanner, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_scanner_refresh) {
            mAdapter.setData(null);
            mAdapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        } else if (item.getItemId() == R.id.menu_by_darken) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/d4rken")));
        } else if (item.getItemId() == R.id.menu_github) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/d4rken/android-appicondetective")));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<ScanItem>> onCreateLoader(int id, Bundle args) {
        return new ScanLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ScanItem>> loader, List<ScanItem> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ScanItem>> loader) {
        mAdapter.setData(null);
        mAdapter.notifyDataSetChanged();
    }
}
