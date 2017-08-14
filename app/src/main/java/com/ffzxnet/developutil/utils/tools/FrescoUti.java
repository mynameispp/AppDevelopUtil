package com.ffzxnet.developutil.utils.tools;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.application.MyApplication;

import static com.facebook.imagepipeline.request.ImageRequestBuilder.newBuilderWithSource;

/**
 * 创建者： feifan.pi 在 2017/2/21.
 */

public class FrescoUti {
    /**
     * 加载图片（默认支持gif）
     *
     * @param uri        地址
     * @param draweeView SimpleDraweeView
     */
    public static void load(final Uri uri, final SimpleDraweeView draweeView) {
        ImageRequest request =
                newBuilderWithSource(uri)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }

    /**
     * 加载图片（默认支持gif）
     *
     * @param uri        地址
     * @param draweeView SimpleDraweeView
     * @param width      重新绘制图片宽度
     * @param height     重新绘制图片高度
     */
    public static void load(final String uri, final SimpleDraweeView draweeView, int width, int height) {
        ImageRequest request =
                newBuilderWithSource(Uri.parse(uri))
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setResizeOptions(new ResizeOptions(width, height))
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }

    /**
     * 加载图片（默认支持gif）
     *
     * @param uri        地址
     * @param draweeView SimpleDraweeView
     * @param width      重新绘制图片宽度
     * @param height     重新绘制图片高度
     * @param listener   图片加载监听
     */
    public static void load(Uri uri, SimpleDraweeView draweeView, int width, int height,
                            BaseControllerListener listener) {
        ImageRequest request =
                newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width, height))
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setControllerListener(listener)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }

    /**
     * 用于根据图片比例的大小显示图片
     *
     * @param simpleDraweeView
     * @param imagePath
     * @param imageWidth
     */
    public static void setControllerListener(final SimpleDraweeView simpleDraweeView, String imagePath, final int imageWidth) {
        final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = imageWidth;

                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                simpleDraweeView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
                layoutParams.width = ScreenUtils.dip2px(simpleDraweeView.getContext(), 164);
                layoutParams.height = ScreenUtils.dip2px(simpleDraweeView.getContext(), 164);
                simpleDraweeView.setLayoutParams(layoutParams);
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setOldController(simpleDraweeView.getController())
                .setUri(Uri.parse(imagePath)).build();
        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        hierarchy.setPlaceholderImage(R.mipmap.icon_default_post_img);
        hierarchy.setFailureImage(R.mipmap.icon_default_post_img);
        controller.setHierarchy(hierarchy);
        simpleDraweeView.setController(controller);
    }

    /**
     * 用于根据图片比例的大小显示图片
     *
     * @param simpleDraweeView
     * @param imagePath
     */
    public static void setControllerListener(final SimpleDraweeView simpleDraweeView, String imagePath) {
        final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                float aspectRatio = 1f;
                int imageHeight = imageInfo.getHeight();
                int imageWidth = imageInfo.getWidth();
                int viewWidth = 0;
                if (imageHeight > imageWidth) {
                    viewWidth = (int) (MyApplication.Screen_Width * 0.7);
                } else {
                    viewWidth = MyApplication.Screen_Width;
                }
                aspectRatio = ((float)imageWidth) / imageHeight;
                layoutParams.width = viewWidth;
                layoutParams.height = (int) (viewWidth/aspectRatio);
                simpleDraweeView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
                layoutParams.width = ScreenUtils.dip2px(simpleDraweeView.getContext(), 164);
                layoutParams.height = ScreenUtils.dip2px(simpleDraweeView.getContext(), 164);
                simpleDraweeView.setLayoutParams(layoutParams);
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setOldController(simpleDraweeView.getController())
                .setUri(Uri.parse(imagePath)).build();
        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        hierarchy.setPlaceholderImage(R.mipmap.icon_default_post_img);
        hierarchy.setFailureImage(R.mipmap.icon_default_post_img);
        controller.setHierarchy(hierarchy);
        simpleDraweeView.setController(controller);
    }
}
