package cn.postwall.blog.controller.user;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.dto.ArticleDTO;
import cn.postwall.blog.pojo.po.*;
import cn.postwall.blog.service.*;
import cn.postwall.blog.utils.BeanHelper;
import cn.postwall.blog.utils.DateHelper;
import cn.postwall.blog.utils.FileHelper;
import cn.postwall.blog.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author liuhanchao
 * @date 2022/12/26 10:23
 * @Description:
 */
@Controller
public class ArticleController {

    @Autowired
    private IBlogArticleService articleService;

    @Autowired
    private IBlogTagService tagService;

    @Autowired
    private IBlogArticleTagService articleTagService;

    @Autowired
    private IBlogUserService userService;

    @Autowired
    private IBlogArticleCommentService commentService;

    @Autowired
    private IBlogArticlePraiseService articlePraiseService;


    @PostMapping("/insertArticle")
    @ResponseBody
    public Result insertArticle(@RequestBody ArticleDTO articleDTO) {
        long currentUserId = JwtHelper.getCurrentUserId();
        if (currentUserId == -1) {
            return Result.fail("请先登录");
        }
        BlogUserPO userById = userService.findUserById(currentUserId);
        if (userById == null) {
            return Result.fail("用户不存在");
        }

        String nowYear = DateHelper.getNowYear();
        String nowMonth = DateHelper.getNowMonth();
        String nowDay = DateHelper.getNowDay();
        String link = nowYear + "/" + nowMonth + "/" + nowDay + "/" + articleDTO.getTitle();

        BlogArticlePO articleByLink = articleService.findArticleByLink(link);
        if (articleByLink != null) {
            return Result.fail("该文章标题已存在");
        }

        BlogArticlePO blogArticlePO = BeanHelper.copyProperties(articleDTO, BlogArticlePO.class);

        // 判断上传的图片是否为base64编码
        String fileSuffix = FileHelper.isBase64(articleDTO.getBase64());
        if (fileSuffix != null) {
            // 保存base64图片
            String coverUrl = FileHelper.saveBase64Image(articleDTO.getBase64());
            if (coverUrl != null) {
                blogArticlePO.setCoverUrl(coverUrl);
            }
        }

        blogArticlePO.setYear(nowYear);
        blogArticlePO.setMonth(nowMonth);
        blogArticlePO.setDay(nowDay);
        blogArticlePO.setPublishDate(DateHelper.getYDM());

        blogArticlePO.setLink(link);

        blogArticlePO.setCreateUser(currentUserId);
        blogArticlePO.setUpdateUser(currentUserId);

        boolean flag = articleService.insertArticle(blogArticlePO);
        if (flag) {
            // 标签
            for (String tag : articleDTO.getTags()) {
                BlogTagPO tagByName = tagService.findTagByName(tag);
                if (tagByName != null) {
                    BlogArticleTagPO articleTagPO = new BlogArticleTagPO();
                    articleTagPO.setArticleId(blogArticlePO.getId());
                    articleTagPO.setTagId(tagByName.getId());
                    articleTagService.saveArticleTag(articleTagPO);
                }
            }
         return Result.success("文章发表成功");
        }
        return Result.fail("文章保存失败，请重试");
    }

    @PostMapping("/updateArticle")
    @ResponseBody
    public Result updateArticle(@RequestBody ArticleDTO articleDTO) {
        long currentUserId = JwtHelper.getCurrentUserId();
        if (currentUserId == -1) {
            return Result.fail("请先登录");
        }
        BlogUserPO userById = userService.findUserById(currentUserId);
        if (userById == null) {
            return Result.fail("用户不存在");
        }
        BlogArticlePO articleById = articleService.findArticleById(articleDTO.getId());
        if (articleById == null) {
            return Result.fail("文章不存在");
        }

        if (!articleById.getTitle().equals(articleDTO.getTitle())) {
            String link = articleById.getYear() + "/" + articleById.getMonth() + "/" + articleById.getDay() + "/" + articleDTO.getTitle();
            BlogArticlePO articleByLink = articleService.findArticleByLink(link);
            if (articleByLink != null) {
                return Result.fail("该文章标题已存在");
            }
            articleById.setLink(link);
        }

        // 判断上传的图片是否为base64编码
        String fileSuffix = FileHelper.isBase64(articleDTO.getBase64());
        if (fileSuffix != null) {
            // 保存base64图片
            String coverUrl = FileHelper.saveBase64Image(articleDTO.getBase64());
            if (coverUrl != null) {
                articleById.setCoverUrl(coverUrl);
            }
        }

        articleById.setTitle(articleDTO.getTitle());
        articleById.setCategoryId(articleDTO.getCategoryId());
        articleById.setContent(articleDTO.getContent());
        articleById.setUpdateUser(currentUserId);

        boolean flag = articleService.updateBlogArticle(articleById);
        if (flag) {
            List<String> tags = new ArrayList<>(Arrays.asList(articleDTO.getTags()));
            List<BlogArticleTagPO> tagByArticle = articleTagService.findTagByArticle(articleById.getId());
            // 用户存放删除的标签
            List<Long> deleteTag = new ArrayList<>();
            for (BlogArticleTagPO blogArticleTagPO : tagByArticle) {
                BlogTagPO tagById = tagService.findTagById(blogArticleTagPO.getTagId());
                if (tags.contains(tagById.getName())) {
                    tags.remove(tagById.getName());
                } else {
                    deleteTag.add(blogArticleTagPO.getId());
                }
            }
            // 删除标签
            for (Long id : deleteTag) {
                articleTagService.deleteTagById(id);
            }

            // 新增标签
            for (String tag : tags) {
                BlogTagPO tagByName = tagService.findTagByName(tag);
                if (tagByName != null) {
                    BlogArticleTagPO articleTagPO = new BlogArticleTagPO();
                    articleTagPO.setArticleId(articleById.getId());
                    articleTagPO.setTagId(tagByName.getId());
                    articleTagService.saveArticleTag(articleTagPO);
                }
            }
            return Result.success("文章修改成功");
        }

        return Result.fail("文章修改失败，请重试");
    }

    @GetMapping("/deleteArticle")
    @ResponseBody
    public Result updateArticleIsDelete(@RequestParam("id") long id) {
        long currentUserId = JwtHelper.getCurrentUserId();
        if (currentUserId == -1) {
            return Result.fail("请先登录");
        }
        BlogUserPO userById = userService.findUserById(currentUserId);
        if (userById == null) {
            return Result.fail("用户不存在");
        }
        BlogArticlePO articleById = articleService.findArticleById(id);
        if (articleById == null) {
            return Result.fail("文章不存在，无法删除");
        }

        if (articleById.getCreateUser() != currentUserId) {
            return Result.fail("无法删除非自己的文章");
        }

        boolean flag = articleService.updateIsDelete(id, SysConstant.IS_DELETE.DISABLE);

        if (flag) {
            return Result.success("文章[" + articleById.getTitle() +"]删除成功");
        }
        return Result.fail("文章[" + articleById.getTitle() +"]删除成功");
    }


    @PostMapping("/pageArticles")
    @ResponseBody
    public Result pageArticles(String title, Long categoryId, @RequestParam("curPage") int curPage, @RequestParam("pageSize") int pageSize) {
        List<BlogArticlePO> blogArticlePOS = articleService.pageUserArticles(title, categoryId, null, curPage, pageSize);
        if (blogArticlePOS.size() > 0) {
            int count = articleService.countUserArticles(title, categoryId, null);
            Map map = new HashMap<>();
            map.put("articles", blogArticlePOS);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }

    @PostMapping("/pageArticlesByTag")
    @ResponseBody
    public Result pageArticlesByTag(@RequestParam("tagName") String tagName, @RequestParam("curPage") int curPage, @RequestParam("pageSize") int pageSize) {
        BlogTagPO tagByName = tagService.findTagByName(tagName);
        if (tagByName == null) {
            return Result.fail("标签不存在");
        }
        List<BlogArticlePO> blogArticlePOS = articleService.pageBlogArticleTag(tagByName.getId(), curPage, pageSize);
        if (blogArticlePOS.size() > 0) {
            int count = articleTagService.countBlogArticleTag(tagByName.getId());
            Map map = new HashMap<>();
            map.put("articles", blogArticlePOS);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }

    @GetMapping("/myArticles")
    @ResponseBody
    public Result myArticles(String title, @RequestParam("curPage") int curPage, @RequestParam("pageSize") int pageSize) {
        long currentUserId = JwtHelper.getCurrentUserId();
        List<BlogArticlePO> blogArticlePOS = articleService.pageUserArticles(title, null, currentUserId, curPage, pageSize);
        if (blogArticlePOS.size() > 0) {
            int count = articleService.countUserArticles(title, null, currentUserId);
            Map map = new HashMap<>();
            map.put("articles", blogArticlePOS);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }

    @GetMapping("/getArticle/{year}/{month}/{day}/{title}")
    @ResponseBody
    public Result getArticle(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String title) {
        String link = year + "/" + month + "/" + day + "/" + title;
        BlogArticlePO articleByLink = articleService.findArticleByLink(link);
        if (articleByLink != null) {
            articleService.increaseReading(articleByLink.getId());
            return Result.success(articleByLink);
        }
        return Result.fail("没有找到文章");
    }

    @PostMapping("/commentArticle")
    @ResponseBody
    public Result commentArticle(@RequestBody BlogArticleCommentPO commentPO) {
        long currentUserId = JwtHelper.getCurrentUserId();
        if (currentUserId == -1) {
            return Result.fail("请先登录");
        }
        BlogUserPO userById = userService.findUserById(currentUserId);
        if (userById == null) {
            return Result.fail("用户不存在");
        }
        BlogArticlePO articleById = articleService.findArticleById(commentPO.getArticleId());
        if (articleById == null || SysConstant.IS_DELETE.DISABLE == articleById.getIsDelete() || articleById.getIsPublish() != 1) {
            return Result.fail("文章不存在");
        }
        if (articleById.getIsComment() != 1) {
            return Result.fail("文章禁止评论");
        }
        // 评论用户
        commentPO.setUserId(currentUserId);
        // 判断是否为回复评论
        if (commentPO.getParentId() != 0) {
            BlogArticleCommentPO articleCommentById = commentService.findArticleCommentById(commentPO.getParentId());
            if (articleCommentById.getBannerId() == 0) {
                commentPO.setBannerId(articleCommentById.getId());
            } else {
                commentPO.setBannerId(articleCommentById.getBannerId());
            }
        } else {
            commentPO.setBannerId(0L);
        }
        boolean flag = commentService.comment(commentPO);
        if (flag) {
            articleService.updateCommentNum(commentPO.getArticleId(), true);
            return Result.success("评论成功");
        }
        return Result.fail("评论失败");
    }

    @PostMapping("/getCommentByArticleId")
    @ResponseBody
    public Result getCommentByArticleId(@RequestParam("id") Integer articleId, @RequestParam("curPage") Integer curPage, @RequestParam("pageSize") Integer pageSize) {
        List<BlogArticleCommentPO> commentByArticleId = commentService.findCommentByArticleId(articleId, curPage, pageSize);
        if (commentByArticleId.size() > 0) {
            Integer count = commentService.countArticleCommentParentByArticleId(articleId);
            Map<String, Object> map = new HashMap<>();
            map.put("comments", commentByArticleId);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有评论");
    }

    @GetMapping("/praiseArticle")
    @ResponseBody
    public Result praiseArticle(@RequestParam("articleId") Long articleId, @RequestParam("userId") Long userId) {
        BlogArticlePO articleById = articleService.findArticleById(articleId);
        if (articleById == null) {
           return Result.fail("文章不存在");
        }
        BlogArticlePraisePO articlePraise = articlePraiseService.findArticlePraise(articleId, userId);
        if (articlePraise == null) {
            BlogArticlePraisePO blogArticlePraisePO = new BlogArticlePraisePO(articleId, userId);
            articlePraiseService.insertBlogArticlePraise(blogArticlePraisePO);
            articleService.increasePraise(articleId, true);
            return Result.success(true,"点赞成功");
        } else {
            articlePraiseService.cancelPraise(articlePraise.getId());
            articleService.increasePraise(articleId, false);
            return Result.success(false,"取消点赞成功");
        }
    }

}
