/*
 * Copyright (C) 2015 RECRUIT LIFESTYLE CO., LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ffzxnet.developutil.ui.refresh.wave_swipe_refresh_layout.code;

import android.content.Context;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatImageView;

class AnimationImageView extends AppCompatImageView {

    /**
     * Animation的Start和结束时在Listener
     */
    private Animation.AnimationListener mListener;

    /**
     * {@inheritDoc}
     */
    public AnimationImageView(Context context) {
        super(context);
    }

    /**
     * {@link AnimationImageView#mListener}
     *
     * @param listener {@link Animation.AnimationListener}
     */
    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    /**
     * {@link Animation.AnimationListener#onAnimationStart(Animation)}
     */
    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    /**
     * {@link Animation.AnimationListener#onAnimationEnd(Animation)}
     */
    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }
}