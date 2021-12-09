package id.co.bspguard.android.bravo.fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import id.co.bspguard.android.bravo.R;

public abstract class BaseFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(getLayoutId(), container, false);
    initView(root);
    return root;
  }

  protected abstract int getLayoutId();

  protected void initView(View root) {
  }

}
