import request from "@/utils/request";

export function getTagList() {
  return request({
    url: "/getTagList",
    method: "get",
  });
}
