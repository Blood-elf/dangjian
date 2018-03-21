/**
 *
 * @authors Vicky Hong (Vicky198808@163.com)<div></div>
 */

$(function(){
  $('#xqdjPop').click(function(){
    $('.overlay').show();
    $('.xqdjPopShow').show();
  })
  $('.btnSure').click(function(){
    $('.overlay').hide();
    $('.xqdjPopShow').hide();
    var popValue="";
    $(".xqdjPopShow").find("input:checked").each(function(){
    	popValue+=$(this).val()+",";
    });
    $("#xqdjPop").val(popValue.substring(0, popValue.length-1));
  });
  $('.btnCancel').click(function(){
    $('.overlay').hide();
    $('.xqdjPopShow').hide();
  })
})