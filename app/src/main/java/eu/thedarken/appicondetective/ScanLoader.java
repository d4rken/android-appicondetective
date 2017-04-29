package eu.thedarken.appicondetective;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ScanLoader extends AsyncTaskLoader<List<ScanItem>> {
    private List<ScanItem> mData;
    private static final Comparator<ScanItem> SORTER_PXCOUNT = new Comparator<ScanItem>() {

        public int compare(ScanItem f1, ScanItem f2) {
            return ((Integer) f2.getPxCount()).compareTo(f1.getPxCount());
        }
    };

    public ScanLoader(Activity activity) {
        super(activity);
    }

    @Override
    public void deliverResult(List<ScanItem> data) {
        if (isReset()) return;
        this.mData = data;
        if (isStarted()) super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) deliverResult(mData);
        if (takeContentChanged() || mData == null) forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mData = null;
    }

    @Override
    public List<ScanItem> loadInBackground() {
        PackageManager pm = getContext().getPackageManager();
        List<ScanItem> result = new ArrayList<>();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo pkg : packages) {
            ScanItem newItem = new ScanItem(pkg.packageName);
            if (pkg.applicationInfo != null) {
                Drawable icon = pkg.applicationInfo.loadIcon(pm);
                if (icon instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
                    newItem.setHeight(bitmap.getHeight());
                    newItem.setWidth(bitmap.getWidth());
                } else if (icon instanceof StateListDrawable) {
                    Drawable.ConstantState constantState = icon.mutate().getConstantState();
                    if (constantState instanceof DrawableContainer.DrawableContainerState) {
                        DrawableContainer.DrawableContainerState drwblContainerState = (DrawableContainer.DrawableContainerState) constantState;
                        for (Drawable drwbl : drwblContainerState.getChildren()) {
                            if (drwbl instanceof BitmapDrawable) {
                                Bitmap bitmap = ((BitmapDrawable) drwbl).getBitmap();
                                newItem.setHeight(bitmap.getHeight());
                                newItem.setWidth(bitmap.getWidth());
                                break;
                            }
                        }
                    }
                } else if (icon instanceof VectorDrawable || icon instanceof VectorDrawableCompat) {
                    newItem.setHeight(icon.getIntrinsicHeight());
                    newItem.setWidth(icon.getIntrinsicWidth());
                }

                newItem.setType(icon.getClass().getSimpleName());
            }
            if (pkg.applicationInfo != null) {
                CharSequence name = pm.getApplicationLabel(pkg.applicationInfo);
                if (name != null) newItem.setName(name.toString());
            }
            result.add(newItem);
        }
        Collections.sort(result, SORTER_PXCOUNT);
        return result;
    }
}
