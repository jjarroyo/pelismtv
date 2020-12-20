package com.jj.pelismtv.ui.onboarding

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.leanback.app.OnboardingSupportFragment
import com.jj.pelismtv.R
import java.util.*

class OnboardingFragment : OnboardingSupportFragment() {

    companion object {
        val COMPLETED_ONBOARDING = "completed_onboarding"
    }


    private val pageTitles = intArrayOf(
        R.string.onboarding_title_welcome,
        R.string.onboarding_title_design,
        R.string.onboarding_title_simple,
        R.string.onboarding_title_project
    )
    private val pageDescriptions = intArrayOf(
        R.string.onboarding_description_welcome,
        R.string.onboarding_description_design,
        R.string.onboarding_description_simple,
        R.string.onboarding_description_project
    )
    private val pageImages = intArrayOf(
        R.drawable.tv_animation_a,
        R.drawable.tv_animation_b,
        R.drawable.tv_animation_c,
        R.drawable.tv_animation_d
    )

    private val ANIMATION_DURATION: Long = 500
    private var mContentAnimator: Animator? = null
    private var mContentView: ImageView? = null

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Set the logo to display a splash animation
     //   logoResourceId = R.drawable.videos_by_google_banner
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @Nullable
    override fun onFinishFragment() {
        super.onFinishFragment()
        // Our onboarding is done
        // Update the shared preferences
        val sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
        sharedPreferencesEditor.putBoolean(COMPLETED_ONBOARDING, true)
        sharedPreferencesEditor.apply()
        // Let's go back to the MainActivity
        requireActivity().finish()
    }
    override fun getPageCount(): Int {
        return pageTitles.size
    }

    override fun getPageTitle(pageIndex: Int): CharSequence {
        return getString(pageTitles.get(pageIndex))
    }

    override fun getPageDescription(pageIndex: Int): CharSequence {
        return getString(pageDescriptions.get(pageIndex))
    }
    @Nullable
    override fun onCreateBackgroundView(inflater: LayoutInflater?, container: ViewGroup?): View? {
        val bgView = View(activity)
        bgView.setBackgroundColor(resources.getColor(R.color.fastlane_background))
        return bgView
    }
    @Nullable
    override fun onCreateContentView(inflater: LayoutInflater?, container: ViewGroup?): View? {
        mContentView = ImageView(activity)
        mContentView!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
        mContentView!!.setPadding(0, 32, 0, 32)
        return mContentView
    }
    @Nullable
    override fun onCreateForegroundView(inflater: LayoutInflater?, container: ViewGroup?): View? {
        return null
    }
    override fun onPageChanged(newPage: Int, previousPage: Int) {
        mContentAnimator?.end()
        val animators = ArrayList<Animator>()
        val fadeOut: Animator = createFadeOutAnimator(mContentView)
        fadeOut.addListener(object : AnimatorListenerAdapter() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onAnimationEnd(animation: Animator) {
                mContentView!!.setImageDrawable(resources.getDrawable(pageImages[newPage]))
                (mContentView!!.drawable as AnimationDrawable).start()
            }
        })
        animators.add(fadeOut)
        animators.add(createFadeInAnimator(mContentView))
        val set = AnimatorSet()
        set.playSequentially(animators)
        set.start()
        mContentAnimator = set
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateEnterAnimation(): Animator? {
        mContentView!!.setImageDrawable(resources.getDrawable(pageImages[0]))
        (mContentView!!.drawable as AnimationDrawable).start()
        mContentAnimator = createFadeInAnimator(mContentView!!)
        return mContentAnimator
    }

    private fun createFadeInAnimator(view: View?): Animator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f).setDuration(ANIMATION_DURATION)
    }

    private fun createFadeOutAnimator(view: View?): Animator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f).setDuration(ANIMATION_DURATION)
    }
}