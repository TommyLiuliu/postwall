import request from "@/utils/request";

export function pageArticleComment(data) {
    return request({
        url: "/admin/pageArticleComment",
        method: "post",
        params: {
            isDelete: data.isDelete,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    });
}

export function updateCommentIsDelete(id, isDelete) {
    return request({
        url: "/admin/updateCommentIsDelete",
        method: "get",
        params: {
            id: id,
            isDelete: isDelete
        }
    });
}
