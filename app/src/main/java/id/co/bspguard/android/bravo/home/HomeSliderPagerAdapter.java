package id.co.bspguard.android.bravo.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.news.MainNews;

import java.util.List;

public class HomeSliderPagerAdapter extends PagerAdapter {

    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<SliderUtil> imgUrl;
    private ImageLoader imageLoader;
    //private Integer[] images = {R.drawable.slider1,R.drawable.slider2,R.drawable.slider3,R.drawable.slider4};

    public HomeSliderPagerAdapter(List<SliderUtil> imgUrl, Context ctx) {
        this.imgUrl = imgUrl;
        this.ctx = ctx;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = layoutInflater.inflate(R.layout.image_slider_home, null);
        final SliderUtil util = imgUrl.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imgView);



        //imageView.setImageResource(images[position]);
        try {
            imageLoader = CustomVolleyRequest.getInstance(ctx).getImageLoader();
            imageLoader.get(util.getSliderImgUrl(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            /*Glide.with(ctx).load(util).asBitmap().centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);*/

            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent iSlider = new Intent(v.getContext(), MainNews.class);
                    v.getContext().startActivity(iSlider);
                }
            });

        } catch (Exception ex){
            ex.printStackTrace();
        }
        container.addView(view);

//        container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"testing", Toast.LENGTH_LONG );
//            }
//        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((View) object);
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
