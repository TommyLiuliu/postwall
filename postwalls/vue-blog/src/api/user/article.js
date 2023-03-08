import request from "@/utils/request";

export function insertArticle(data) {
    return request({
        url: "/insertArticle",
        method: "post",
        data
    });
}

export function updateArticle(data) {
    return request({
        url: "/updateArticle",
        method: "post",
        data
    });
}

export function pageArticles(data) {
    return request({
        url: "/pageArticles",
        method: "post",
        params: {
            title: data.title,
            categoryId: data.categoryId,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    });
}

export function pageArticlesByTag(data) {
    return request({
        url: "/pageArticlesByTag",
        method: "post",
        params: {
            tagName: data.tagName,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    });
}

export function myArticles(data) {
    return request({
        url: "/myArticles",
        method: "get",
        params: {
            title: data.title,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    });
}

export function getArticle(year, month, day, title) {
    return request({
        url: "/getArticle/" + year + "/" + month + "/" + day + "/" + title,
        method: "get",
    });
}

export function commentArticle(data) {
    return request({
        url: "/commentArticle",
        method: "post",
        data
    });
}

export function getCommentByArticleId(data) {
    return request({
        url: "/getCommentByArticleId",
        method: "post",
        params: {
            id: data.id,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    });
}

export function praiseArticle(articleId, userId) {
    return request({
        url: "/praiseArticle",
        method: "get",
        params: {
            articleId: articleId,
            userId: userId
        }
    });
}

export function deleteArticle(id) {
    return request({
        url: "/deleteArticle",
        method: "get",
        params: {
            id: id
        }
    });
}

export function upload(data) {
    return request({
        url: "/upload",
        method: "post",
        data
    });
}
