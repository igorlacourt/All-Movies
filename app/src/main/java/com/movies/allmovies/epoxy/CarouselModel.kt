package com.movies.allmovies.epoxy

import com.airbnb.epoxy.Carousel
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelWithView
import com.airbnb.epoxy.EpoxyModelClass


/**
 * Created by Josh Laird on 05/05/2017.
 */
@EpoxyModelClass
abstract class CarouselModel : EpoxyModelWithView<Carousel>() {
    @EpoxyAttribute
    var models: List<EpoxyModel<*>>? = null
    @EpoxyAttribute
    var numItemsExpectedOnDisplay: Int = 0
    @EpoxyAttribute(hash = false)
    var recycledViewPool: RecyclerView.RecycledViewPool? = null

    override fun bind(carousel: Carousel) {
        // If there are multiple carousels showing the same item types, you can benefit by having a
        // shared view pool between those carousels
        // so new views aren't created for each new carousel.
        if (recycledViewPool != null) {
            carousel.setRecycledViewPool(recycledViewPool)
        }

        if (numItemsExpectedOnDisplay != 0) {
            carousel.setInitialPrefetchItemCount(numItemsExpectedOnDisplay)
        }

        carousel.setModels(models!!)
    }

    override fun unbind(carousel: Carousel) {
//        carousel.clearModels()
        carousel.clear()
    }

    override fun buildView(parent: ViewGroup): Carousel {
        return Carousel(parent.context, null)
    }

    override fun shouldSaveViewState(): Boolean {
        // Save the state of the scroll position
        return true
    }
}