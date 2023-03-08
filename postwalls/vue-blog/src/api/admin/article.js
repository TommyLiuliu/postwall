import request from "@/utils/request";

export function pageAdminArticles(data) {
    return request({
        url: "/admin/pageAdminArticles",
        method: "post",
        params: {
            title: data.title,
            startTime: data.startTime,
            endTime: data.endTime,
            isDelete: data.isDelete,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    });
}

export function updateArticleIsDelete(id, isDelete) {
    return request({
        url: "/admin/updateArticleIsDelete",
        method: "get",
        params: {
            id: id,
            isDelete: isDelete
        }
    });
}

export function updatePublishState(publishState, articleId) {
    return request({
        url: 'admin/updateArticleState',
        method: 'get',
        params: {
            publishState,
            articleId,
        },
    })
}

export function updateCommentState(commentState, articleId) {
    return request({
        url: 'admin/updateArticleState',
        method: 'get',
        params: {
            commentState,
            articleId,
        },
    })
}

export function updateTopState(topState, articleId) {
    return request({
        url: 'admin/updateArticleState',
        method: 'get',
        params: {
            topState,
            articleId,
        },
    })
}
