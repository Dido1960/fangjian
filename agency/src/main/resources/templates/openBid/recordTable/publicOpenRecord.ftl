<script>
    /**
     * 更新开标记录表备注
     * @param e
     */
    function updateDesc(e) {
        var desc = $(e).val().trim();
        $.ajax({
            type: "post",
            url: "${ctx}/staff/updateTenderDocById",
            cache: false,
            data: {
                "id": "${tenderDoc.id}",
                "openBidRecordDes": desc
            },
            success: function (data) {
                window.layer.msg("修改成功!",{icon:1});
            }
        });
    }
</script>