package com.fucang.mobileplayer.bean;

/**
 * Created by 浮滄 on 2017/5/11.
 */

import java.util.List;

/**
 * 推荐数据bean
 */
public class GroomBean {

    private InfoBean info;
    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {
        /**
         * count : 4380
         * np : 1494463022
         */

        private int count;
        private int np;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNp() {
            return np;
        }

        public void setNp(int np) {
            this.np = np;
        }
    }

    public static class ListBean {
        private int status;
        private String comment;
        private String bookmark;
        private String text;
        private GifBean gif;
        private String up;
        private String share_url;
        private String down;
        private String forward;
        private UBean u;
        private String passtime;
        private String type;
        private String id;
        private VideoBean video;
        private ImageBean image;
        private List<TagsBean> tags;
        private List<?> top_comments;
        private List<VoteBean> vote;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getBookmark() {
            return bookmark;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public GifBean getGif() {
            return gif;
        }

        public void setGif(GifBean gif) {
            this.gif = gif;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getDown() {
            return down;
        }

        public void setDown(String down) {
            this.down = down;
        }

        public String getForward() {
            return forward;
        }

        public void setForward(String forward) {
            this.forward = forward;
        }

        public UBean getU() {
            return u;
        }

        public void setU(UBean u) {
            this.u = u;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<?> getTop_comments() {
            return top_comments;
        }

        public void setTop_comments(List<?> top_comments) {
            this.top_comments = top_comments;
        }

        public List<VoteBean> getVote() {
            return vote;
        }

        public void setVote(List<VoteBean> vote) {
            this.vote = vote;
        }

        public static class GifBean {
            /**
             * images : ["http://wimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5.gif","http://dimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5.gif"]
             * width : 235
             * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5_a_1.jpg","http://dimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5_a_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5_d.jpg","http://dimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5_d.jpg","http://wimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5_a_1.jpg","http://dimg.spriteapp.cn/ugc/2017/05/10/59132ffaf1df5_a_1.jpg"]
             * height : 291
             */

            private int width;
            private int height;
            private List<String> images;
            private List<String> gif_thumbnail;
            private List<String> download_url;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public List<String> getGif_thumbnail() {
                return gif_thumbnail;
            }

            public void setGif_thumbnail(List<String> gif_thumbnail) {
                this.gif_thumbnail = gif_thumbnail;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }
        }

        public static class UBean {
            /**
             * header : ["http://wimg.spriteapp.cn/profile/large/2017/04/02/58e097d979460_mini.jpg","http://dimg.spriteapp.cn/profile/large/2017/04/02/58e097d979460_mini.jpg"]
             * uid : 15166779
             * is_vip : false
             * is_v : false
             * room_url :
             * room_name : 百思不得姐
             * room_role : 帮主
             * room_icon : http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_3.png
             * name : 搞笑刚哥 [百思不得姐]
             */

            private String uid;
            private boolean is_vip;
            private boolean is_v;
            private String room_url;
            private String room_name;
            private String room_role;
            private String room_icon;
            private String name;
            private List<String> header;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public boolean isIs_v() {
                return is_v;
            }

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public String getRoom_url() {
                return room_url;
            }

            public void setRoom_url(String room_url) {
                this.room_url = room_url;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_role() {
                return room_role;
            }

            public void setRoom_role(String room_role) {
                this.room_role = room_role;
            }

            public String getRoom_icon() {
                return room_icon;
            }

            public void setRoom_icon(String room_icon) {
                this.room_icon = room_icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getHeader() {
                return header;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }
        }

        public static class VideoBean {
            /**
             * playfcount : 166
             * height : 480
             * width : 852
             * video : ["http://wvideo.spriteapp.cn/video/2017/0510/c1e8c756-357d-11e7-ae6a-d4ae5296039dcut_wpd.mp4","http://dvideo.spriteapp.cn/video/2017/0510/c1e8c756-357d-11e7-ae6a-d4ae5296039dcut_wpd.mp4"]
             * download : ["http://wvideo.spriteapp.cn/video/2017/0510/c1e8c756-357d-11e7-ae6a-d4ae5296039dcut_wpc.mp4","http://dvideo.spriteapp.cn/video/2017/0510/c1e8c756-357d-11e7-ae6a-d4ae5296039dcut_wpc.mp4"]
             * duration : 166
             * playcount : 8172
             * thumbnail : ["http://wimg.spriteapp.cn/picture/2017/0510/24922706_587.gif","http://dimg.spriteapp.cn/picture/2017/0510/24922706_587.gif"]
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/picture/2017/0510/24922706_587.gif","http://dimg.spriteapp.cn/crop/150x150/picture/2017/0510/24922706_587.gif"]
             */

            private int playfcount;
            private int height;
            private int width;
            private int duration;
            private int playcount;
            private List<String> video;
            private List<String> download;
            private List<String> thumbnail;
            private List<String> thumbnail_small;

            public int getPlayfcount() {
                return playfcount;
            }

            public void setPlayfcount(int playfcount) {
                this.playfcount = playfcount;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getPlaycount() {
                return playcount;
            }

            public void setPlaycount(int playcount) {
                this.playcount = playcount;
            }

            public List<String> getVideo() {
                return video;
            }

            public void setVideo(List<String> video) {
                this.video = video;
            }

            public List<String> getDownload() {
                return download;
            }

            public void setDownload(List<String> download) {
                this.download = download;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class ImageBean {
            /**
             * medium : []
             * big : ["http://wimg.spriteapp.cn/ugc/2017/05/10/5912fdb82c931_1.jpg","http://dimg.spriteapp.cn/ugc/2017/05/10/5912fdb82c931_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2017/05/10/5912fdb82c931_d.jpg","http://dimg.spriteapp.cn/ugc/2017/05/10/5912fdb82c931_d.jpg","http://wimg.spriteapp.cn/ugc/2017/05/10/5912fdb82c931.jpg","http://dimg.spriteapp.cn/ugc/2017/05/10/5912fdb82c931.jpg"]
             * height : 6897
             * width : 750
             * small : []
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/ugc/2017/05/10/5912fdb82c931.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2017/05/10/5912fdb82c931.jpg"]
             */

            private int height;
            private int width;
            private List<?> medium;
            private List<String> big;
            private List<String> download_url;
            private List<?> small;
            private List<String> thumbnail_small;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<?> getMedium() {
                return medium;
            }

            public void setMedium(List<?> medium) {
                this.medium = medium;
            }

            public List<String> getBig() {
                return big;
            }

            public void setBig(List<String> big) {
                this.big = big;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public List<?> getSmall() {
                return small;
            }

            public void setSmall(List<?> small) {
                this.small = small;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class TagsBean {
            /**
             * id : 1
             * name : 搞笑
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class VoteBean {
            /**
             * name : 干得漂亮
             * vid : 57367
             * vote_num : 1307
             */

            private String name;
            private int vid;
            private int vote_num;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getVid() {
                return vid;
            }

            public void setVid(int vid) {
                this.vid = vid;
            }

            public int getVote_num() {
                return vote_num;
            }

            public void setVote_num(int vote_num) {
                this.vote_num = vote_num;
            }
        }
    }
}
