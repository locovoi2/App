package edu.ub.pis.app.view.ui.manage_routines;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    private final List<View> views;

    public ViewPagerAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Your Routines";
        }
        else return "Add new Routine";
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }
}

