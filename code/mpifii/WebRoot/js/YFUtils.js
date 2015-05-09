/*******************************************************************************
 * 扩展js 工具类
 */
(function($) {
	$.YFUtils = {};
	/**
	 * @author JiaYongChao
	 * @description：通用的初始化分页栏的方法
	 * @params：paginationId - 分页栏组件的ID total - 记录总条数 pageNum - 当前页码 pageSize -
	 *                      分页大小 callback - 回调函数，即分页栏下拉框的响应函数
	 */
	$.YFUtils.initializePagination = function initializePagination(
			paginationId, total, pageNum, pageSize) {
		$("#" + paginationId).pagination({});
	}
});