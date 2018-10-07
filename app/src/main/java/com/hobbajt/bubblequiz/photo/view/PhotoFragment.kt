package com.hobbajt.bubblequiz.photo.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.dialogs.ConnectionErrorDialog
import com.hobbajt.bubblequiz.dialogs.NewLevelPackUnlockedDialog
import com.hobbajt.bubblequiz.dialogs.OnDialogClickListener
import com.hobbajt.bubblequiz.extensions.toByteArray
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.mvp.BaseMVPFragment
import com.hobbajt.bubblequiz.photo.model.BubblesSet
import com.hobbajt.bubblequiz.photo.model.dto.LevelState
import com.hobbajt.bubblequiz.photo.view.customview.singleinputview.InputViewContainer
import com.hobbajt.bubblequiz.utilities.AnimationUtilities
import com.hobbajt.bubblequiz.utilities.Utilities
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageSize
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import kotlinx.android.synthetic.main.fragment_photo.*
import javax.inject.Inject

class PhotoFragment: BaseMVPFragment<PhotoPresenter>(), PhotoContract.View
{
    @Inject
    lateinit var presenter: PhotoPresenter

    @Inject
    lateinit var imageLoaderConfiguration: ImageLoaderConfiguration

    @Inject
    lateinit var displayImageOptions: DisplayImageOptions

    private var svImage: PhotoSurfaceView? = null

    // region Lifecycle
    override fun attachView()
    {
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val levelState = savedInstanceState?.getSerializable(LEVEL_STATE_TAG) as LevelState?
        val levelsPack = arguments?.getSerializable(LEVELS_PACK_INFO_TAG) as LevelsPack?

        val levelId = arguments?.getInt(LEVEL_ID_TAG)
        ImageLoader.getInstance().init(imageLoaderConfiguration)
        presenter.onViewCreated(levelsPack, levelId, levelState)
    }

    override fun providePresenter() = presenter

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        val levelState = presenter.onSaveState()
        outState.putSerializable(LEVEL_STATE_TAG, levelState)
    }

    override fun onPause()
    {
        super.onPause()
        Utilities.animateContent(ivAnimationLayer, 0f, 1.0f)
    }
    // endregion

    // region Navigation Hide
    override fun hideAnswerLetters()
    {
        ivAnswer.removeAllViews()
    }

    override fun hideKeyboard()
    {
        activity?.let {
            if(it.currentFocus != null)
            {
                val inputMethodManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(it.currentFocus.windowToken, 0)
            }
        }
    }

    override fun hideLoader()
    {
        pbImageLoader.hide()
        Utilities.animateContent(ivAnimationLayer, 1.0f, 0f)
    }

    override fun hidePhoto()
    {
        ivPhoto.visibility = View.GONE
    }

    override fun hideSurface()
    {
        svImage?.visibility = View.GONE
    }

    override fun hidePlayUI()
    {
        ivRestart.visibility = View.INVISIBLE
        pbAvailableMove.visibility = View.GONE
        ivBomb.visibility = View.GONE
        ivShowPartPhoto.visibility = View.GONE
    }
    // endregion

    // region Navigation Display
    override fun displayLoader()
    {
        pbImageLoader.show()
    }

    override fun displayPointsCount(pointsCount: Int)
    {
        val pointsText = context?.getString(R.string.points)
        tvPoints.text = "$pointsCount $pointsText"
    }

    override fun displayAvailableMovesBar(maxSplits: Float)
    {
        pbAvailableMove.visibility = View.VISIBLE
        pbAvailableMove.max = maxSplits
    }

    override fun displayAvailableMoves(movePoints: Float)
    {
        pbAvailableMove.progress = movePoints
    }

    override fun displayPhoto(photoUrl: String, screenSize: Int)
    {
        ImageLoader.getInstance().loadImage(photoUrl, ImageSize(screenSize, screenSize), object: SimpleImageLoadingListener()
        {
            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?)
            {
                ivPhoto.setImageBitmap(loadedImage)
            }
        })
        ImageLoader.getInstance().displayImage(photoUrl, ivPhoto)
        AnimationUtilities.animatePhotoIn(ivPhoto, svImage)
    }

    override fun displayCorrectAnswer()
    {
        ivAnswer.isEnabled = false
        ivAnswer.displayAnswer()
        context?.let { ivAnswer.setColor(ContextCompat.getColor(it, R.color.green))  }
    }

    override fun displayDownloadErrorDialog(message: String)
    {
        Log.e("error", message)
        context?.let {
            ConnectionErrorDialog(it, object: OnDialogClickListener {
                override fun onPositiveButtonClicked()
                {
                    presenter.onLoadRetryClicked()
                }

                override fun onNegativeButtonClicked()
                {
                    presenter.onLoadCancelClicked()
                }

            }).show()
        }
    }

    override fun displayBubblesSurface(drawItems: BubblesSet, surfaceSize: Int)
    {
        if (clContent.findViewById<PhotoSurfaceView>(R.id.svImage) != null)
            clContent.removeView(svImage)

        svImage = View.inflate(context, R.layout.surface_view, null) as PhotoSurfaceView

        svImage?.let {
            it.layoutParams = ConstraintLayout.LayoutParams(surfaceSize, surfaceSize)
            val constraintSet = ConstraintSet()
            clContent.addView(it, 0)
            constraintSet.clone(clContent)
            it.setZOrderOnTop(true)
            it.holder.setFormat(PixelFormat.TRANSLUCENT)
            it.bind(drawItems.bubbles, surfaceSize, View.OnTouchListener { _, event ->
                presenter.onBubbleTouch(event.x, event.y, event.action)
            })
            it.setView(this)
            it.visibility = View.VISIBLE

            constraintSet.connect(it.id, ConstraintSet.TOP, ivNext.id, ConstraintSet.BOTTOM, 8)
            constraintSet.connect(it.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            constraintSet.connect(it.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            constraintSet.applyTo(clContent)
        }
    }

    override fun displayNewLevelPackUnlockedDialog()
    {
        context?.let { NewLevelPackUnlockedDialog(it).show() }
    }

    override fun displayAnswerFieldEmpty(answer: String)
    {
        ivAnswer.setAnswerPattern(answer)
    }
    // endregion

    // region Animation
    override fun disableAllHintsAnimations()
    {
        ivBomb.clearAnimation()
        ivShowPartPhoto.clearAnimation()
    }

    override fun animateBombHint()
    {
        val animation = AnimationUtils.loadAnimation(context, R.anim.item_shock_infinite)
        ivBomb.startAnimation(animation)
    }

    override fun animateShowPartHint()
    {
        val animation = AnimationUtils.loadAnimation(context, R.anim.item_shock_infinite)
        ivShowPartPhoto.startAnimation(animation)
    }
    // endregion

    // region Fragments Management
    override fun backToLevels()
    {
        activity?.onBackPressed()
    }

    override fun openLevel(levelsPack: LevelsPack, levelId: Int)
    {
        val fragment = newInstance(levelsPack, levelId)
        fragmentsManager.changeFragment(fragment, true)
    }

    companion object
    {
        const val LEVELS_PACK_INFO_TAG = "levelsPackInfo"
        const val LEVEL_ID_TAG = "levelId"
        const val LEVEL_STATE_TAG = "levelState"

        fun newInstance(levelsPack: LevelsPack, levelId: Int): PhotoFragment
        {
            val bundle = Bundle()
            bundle.putSerializable(LEVELS_PACK_INFO_TAG, levelsPack)
            bundle.putInt(LEVEL_ID_TAG, levelId)

            val fragment = PhotoFragment()
            fragment.arguments = bundle
            return fragment

        }
    }
    // endregion

    override fun createListeners()
    {
        ivBomb.setOnClickListener {
            presenter.onBombClicked()
        }

        ivShowPartPhoto.setOnClickListener {
            presenter.onShowPartClicked()
        }

        ivNext.setOnClickListener {
            AnimationUtilities.animate(it, R.animator.item_rotate)
            presenter.onNextClicked()
        }

        ivRestart.setOnClickListener {
            AnimationUtilities.animate(ivRestart, R.animator.item_rotate)
            presenter.onRestartClicked()
        }


        ivAnswer.setOnCompleteListener(object: InputViewContainer.OnCompleteListener {
            override fun onComplete(answer: String)
            {
                presenter.onAnswerWriteComplete(answer)
            }
        })
    }

    override fun loadImage(photoUrl: String, size: Int, listener: OnImageLoadListener)
    {
        ImageLoader.getInstance().loadImage(photoUrl, ImageSize(size, size), displayImageOptions, object : SimpleImageLoadingListener()
        {
            override fun onLoadingComplete(imageUri: String, view: View?, loadedImage: Bitmap?)
            {
                loadedImage?.let {
                    listener.onSuccess(loadedImage.toByteArray())
                }
            }

            override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?)
            {
                failReason?.let { listener.onFailed(it.cause) }
            }
        })
    }
}