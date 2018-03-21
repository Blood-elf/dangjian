/**
 *
 * @authors Vicky Hong (Vicky198808@163.com)
 */

$(function(){
  if(document.getElementById('nav_menu')){
  var navMenu = $('#nav_menu>li:lt(9)');
  $('#nav_menu').one('mouseover',function(){
    $(this).find('img').each(function(){
      var src = this.getAttribute('src2');
      $(this).attr('src',src);
    })
  });
  navMenu.hover(function(){
      $(this).addClass('hover');
  },function(){
      $(this).removeClass('hover');
    });
}
})