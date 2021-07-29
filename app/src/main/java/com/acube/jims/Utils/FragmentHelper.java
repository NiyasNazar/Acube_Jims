package com.acube.jims.Utils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.acube.jims.R;

public class FragmentHelper {
    /**
     * @param activity
     * @param containerId
     * @param fragment
     */
    public static void replaceFragment(final FragmentActivity activity, final int containerId,
                                       final Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack("tag");
        transaction.replace(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceAndAddFragment(final FragmentActivity activity, final int containerId,
                                             final Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(final FragmentActivity activity, final int containerId, final Fragment fragment, int animId) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
     //   transaction.setCustomAnimations(animId, R.anim.right_slide_out);
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(final FragmentActivity activity, final int containerId, final Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void removeFragment(final FragmentActivity activity, final Fragment fragment, int animId) {
        activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    /**
     * Pop the top state off the back stack immediately .
     *
     * @param activity
     */

    public static void popBackStackImmediate(final FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStackImmediate();
    }

    public static void clearBackStack(final FragmentActivity activity) {
        try {
            activity.getSupportFragmentManager().popBackStackImmediate(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static int getStackCount(final FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        return fragmentManager.getBackStackEntryCount();
    }

    public static Fragment getFragment(final FragmentActivity activity, final int containerId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        return fragmentManager.findFragmentById(containerId);
    }

    public static void showDialogFragment(final FragmentActivity activity, final DialogFragment fragment, String tag) {

        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        fragment.show(ft, tag);
    }

    public static void addNullFragment(final FragmentActivity activity, final int containerId, final Fragment fragment) {

      /*  FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
        transaction.setCustomAnimations(R.anim.right_slide_in, R.anim.right_slide_out, R.anim.right_slide_in, R.anim.right_slide_out);
        transaction.addToBackStack(null);
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();*/

    }

}
