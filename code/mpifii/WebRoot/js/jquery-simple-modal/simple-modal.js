/*
---
description: SIMPLE MODAL for jQuery is a small plugin based on original SimpleModal for Mootools. It can be used to generate alert or confirm messages with few lines of code. Confirm configuration involves the use of callbacks to be applied to affirmative action;i t can work in asynchronous mode and retrieve content from external pages or getting the inline content. SIMPLE MODAL is not a lightbox although the possibility to hide parts of its layout may partially make it similar.

license: MIT-style

authors:
- Micha� Buczko
- Marco Dell'Anna

requires:
- jQuery 1.6+

provides:
- SimpleModal
...

* Simple Modal for jQuery
* Version 1.0
*
* Copyright (c) 2011 Micha� Buczko
* Original Simple Modal copyrighted 2011 Marco Dell'Anna - http://www.plasm.it
*
* Requires:
* jQuery http://jquery.com
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use,
* copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following
* conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*
* Log:
* 1.0 - Initial version [Tested on: ie8/ie9/Chrome/Firefox7/Safari]
*/

(function($) {
    var self = null;

    //Attach this new method to jQuery
    $.fn.extend({
        options: null,
        SimpleModal: function(options) {
        	var defaults = {
	        	//id:			   "",//处理多弹窗情况
	            onAppend:      null,
	            offsetTop:     null,
	            afterClose:    null,
	            overlayOpacity:.3,
	            overlayColor:  "#000000",
//	            width:         400,
	            zindexAdd:	   0,
	            draggable:     false,
	            keyEsc:        false,//是否启用esc回退键
	            overlayClick:  false,//点击弹窗以外的区域是否隐藏弹窗
	            closeButton:   true, // X close button
//	            hideHeader:    false,//不再使用该属性去判断，通过是否有title去判断是否要隐藏
	            hideFooter:    false,//不再使用，通过新加的buttons去判断是否显示footer
	            buttons:	   [],
	            animate:	   true,
	            btn_ok:        "OK", // Label
	            btn_cancel:    "Cancel", // Label
	            template:"<div class=\"simple-modal-header\"> \
	                <h1>{_TITLE_}</h1> \
	            </div> \
	                <div class=\"simple-modal-body\"> \
	                <div class=\"contents\">{_CONTENTS_}</div> \
	            </div> \
	                <div class=\"simple-modal-footer\"></div>"
	        }
            var options = $.extend({}, defaults, options);
            generPopId();//处理多弹窗情况
            generPopZIndex();//处理多窗口z-index
            window.popsOption.push(options);//必须放在这个位置
            generOverlay();
			generPop();
			
			var $this = $('#simple-modal-'+options.id);
			$this.options = options;
            for(var i=0;i<options.buttons.length;i++){
        		var buttonOptions = options.buttons[i];
        		$this.addButton(buttonOptions.text, buttonOptions.classe,buttonOptions.clickEvent);
        	}
            
            /**
		     * 将配置参数template中的"{...}"替换为具体的配置参数
		     */
			function replaceParams(content,params) {
		        for(var attr in params) {
		            content=content.replace(new RegExp('{'+attr+'}','g'), params[attr]);
		        }
		        return content;
		    }
		    
			function generPopId(){
				var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
				var id = "";
			    for(var i = 0; i < 5 ; i ++) {
			        var index = Math.floor(Math.random()*(chars.length));
			        id += chars[index];
			    }
			    options.id = id;
			}
			
			function generPopZIndex(){
		    	var thisPopZIndex = 599;
	            if(window.popsOption != undefined){//存储已经弹出的弹框
	            	for(var i=0;i<window.popsOption.length;i++){
	            		if(window.popsOption[i].zindex >= thisPopZIndex){
	            			thisPopZIndex = window.popsOption[i].zindex+1;
	            		}
	            	}
	            }else{
	            	window.popsOption = [];
	            }
	            options.zindex = thisPopZIndex;
		    }
		    
			/**
        	 * 生成遮罩层
        	 */
            function generOverlay(){
            	var overlay = $("<div>")
                        .attr("id", "simple-modal-overlay-"+options.id)
                        .addClass("simple-modal-overlay")
                        .css({"background-color": options.overlayColor, "opacity": 0, "z-index":options.zindex+options.zindexAdd});
                if (options.overlayClick) {
	                overlay.click(function(e) {
	                				var id = $(this).attr("id");
        							id = id.substring(id.lastIndexOf("-")+1);
									$("#simple-modal-"+id).hideModal();
								});
	            }
	            $('body').append(overlay);
            }
			/**
			 * 生成弹窗
			 */
			function generPop(){
				var node = $("<div>")
						.addClass('simple-modal')
						.attr('id', 'simple-modal-'+options.id)
						.css("z-index",options.zindex+1+options.zindexAdd);
				// Set Contents
			    node.html(replaceParams(options.template, {"_TITLE_":options.title || "", "_CONTENTS_":options.contents || ""}));
	            // Custom size Modal
			    if(options.width){
			    	node.css('width', options.width);
			    }
				if(options.height){
					var contents = node.find(".contents");
					contents.css({'height':options.height,'overflow':'auto'});
				}
			    // Add Button X
           		if (options.closeButton){
           			var b = $("<a>").addClass('close').attr({"href": "#"}).text('X').click(function(e) {
		                $(this).parent().hideModal();
		                e.preventDefault();
		            });
		            node.append(b);
           		}
	            $('body').append(node);
			}
        	
			return $this;//返回当前弹窗对象
        },

        /**
         * 提示：该方法依赖调用对象，必须为已配置的弹窗
         * public method showModal
         * Open Modal
         * @options: param to rewrite
         * @return node HTML
         */
        showModal: function() {
        	var thisOptions = this.options;
        	var popObject = $('#simple-modal-'+thisOptions.id);
        	popObject.options = thisOptions;
            // Hide Header &&/|| Footer
        	if (undefined == thisOptions.title){
        		popObject.addClass("hide-header");
        	}
//            if (thisOptions.hideHeader) popObject.addClass("hide-header");
        	if(thisOptions.buttons.length==0){
        		popObject.addClass("hide-footer");
        	}
        	if(thisOptions.model == "loading"){
        		popObject.addClass("loading");
        	}
//            if (thisOptions.hideFooter) popObject.addClass("hide-footer");

            // Enabled Drag Window
            if (thisOptions.draggable) {
                var headDrag = popObject.find('.simple-modal-header'), clicked = false, dx=0, dy=0;
                var updatePos = function(pos) {
                    popObject.css({left: pos.x-dx, top: pos.y-dy});
                };
                var getMousePos = function(e) {
                    return { 'x': e.pageX, 'y': e.pageY };
                };
                headDrag.bind({
                    mousedown: function(e) {
                        var mpos = getMousePos(e), cpos = popObject.position();

                        e.stopPropagation();
                        e.preventDefault();

                        dx = mpos.x - cpos.left;
                        dy = mpos.y - cpos.top;

                        clicked = true;
                    },
                    mouseup: function(e) {
                        e.stopPropagation();
                        e.preventDefault();
                        clicked = false;
                    }
                });
                $(document).mousemove(function(e) {
                    e.stopPropagation();
                    e.preventDefault();
	                if (clicked)
		                updatePos(getMousePos(e));
                });

                // Set handle cursor
                headDrag.css("cursor", "move");
                popObject.addClass("draggable");
            }
            popObject.css({"opacity": 0});
            if(thisOptions.param && thisOptions.param.url && ""!=thisOptions.param.url){
//        	if(thisOptions.model == "modal-ajax"){
				var thisPopId = thisOptions.id;
//				var loadSuccess = false;
				$.ajax({
					type: "POST",
//					async: false,
					data: thisOptions.param.data,
					url: thisOptions.param.url,
					success: function(data,status,xhr){
//						if(data.indexOf("html") != -1 && 
//								data.indexOf("this is mark for login page,do not delete it") != -1){
//							$("#simple-modal-"+thisPopId).hideModal();
//						}else{
//							$('#simple-modal-'+thisPopId+' .contents').html(data);
//							showThisPop();
//							//popObject._display();
////							loadSuccess = true;
//						}
					},
					complete:function(jqXHR,textStatus){
						if(isLoginPage(jqXHR.responseText) || 'success' != textStatus){
							$("#simple-modal-"+thisPopId).hideModal();
						}else{
							$('#simple-modal-'+thisPopId+' .contents').html(jqXHR.responseText);
							showThisPop();
						}
					}
				});
//				if(!loadSuccess){//如果加载失败则移除当前弹窗
//					var optionsIndex = this._popOptionsIndex();
//		        	if(optionsIndex != -1){
//		        		//删除window.popsOption中存储的当前弹窗的配置参数
//						window.popsOption.splice(optionsIndex,1);
//						$('#simple-modal-overlay-'+thisOptions.id).remove();
//						$('#simple-modal-'+thisOptions.id).remove();
//		        	}
//					return;
//				}
        	}else{
        		showThisPop();
        	}
        	
        	function showThisPop(){
        		popObject.css({'left':($(window).width() -popObject.width())/2, 'top': 0-popObject.height(),"opacity": 0});
        		$('#simple-modal-overlay-'+thisOptions.id).animate({opacity: thisOptions.overlayOpacity});
        		if(window.addPopsEvent == undefined || !window.addPopsEvent){
        			$(window).resize($.fn._display);
        			$(document).keyup($.fn._escape);
        			window.addPopsEvent = true;
        		}
        		// Resize Stage
        		popObject._display();
        	}
        },

        /**
         * 提示：该方法依赖调用对象，调用对象必须为已弹窗的jQuery对象（id），或者代有弹窗配置的jQuery对象
         * public method hideModal
         * Close model window
         * return
         */
        hideModal: function() {
        	var thisOptions = this.options;
        	if(undefined == thisOptions){//当前调用的jQuery对象不包含弹窗配置参数，则根据id在弹窗数组中查找配置参数
	        	var optionsIndex = this._popOptionsIndex();
	        	if(optionsIndex != -1){
	        		//删除window.popsOption中存储的当前弹窗的配置参数
					thisOptions = window.popsOption.splice(optionsIndex,1)[0];
	        	}
        	}
        	if(thisOptions){
			    // Remove Overlay
				var container = $('#simple-modal-'+thisOptions.id);
				container.stop();
				var duration = 0;
				if(thisOptions.animate){
					duration = 200;
				}
				container.animate({
					opacity: 0,
					top: 0-container.height()
				}, {
					duration: duration, 
					always: function() {
						$('#simple-modal-overlay-'+thisOptions.id).remove();
						container.remove();
						$.fn._display();
						if(optionsIndex != -1){
							if(thisOptions.afterClose){
								thisOptions.afterClose();
							}
						}
						if(window.popsOption.length == 0){
							$(window).unbind('resize', $.fn._display);
							$(document).unbind('keyup', $.fn._escape);
							window.addPopsEvent = false;
						}
					}
				});
        	}
        },

        /**
         * 提示：该方法依赖调用对象，必须为已配置的弹窗
         * public method addButton
         * Add button to Modal button array
         * require @label:string, @classe:string, @clickEvent:event
         * @return node HTML
         */
        addButton: function(label, classe, clickEvent) {
        	//var $this = this;
            var bt = $('<a>').attr({
                "title" : label,
                "class" : classe
            }).click(clickEvent ? function(e) {
					clickEvent.call($(this).parents(".simple-modal"), e);
				} : function(){
					$(this).parents(".simple-modal").hideModal();
				}).text(label);
			//生成按钮
            var footer = this.find(".simple-modal-footer");
            footer.append(bt);
//            if (options.onAppend) {
//		        options.onAppend.call(this);
//            }
            return this;
        },
		
        /**
         * 获取当前元素的弹窗配置在数组window.popsOption中的位置
         * 提示：该方法依赖调用对象,根据调用的jQuery对象查出该对象的弹窗配置参数
         */
        _popOptionsIndex: function(){
        	var id = this.attr("id");
        	id = id.substring(id.lastIndexOf("-")+1);
        	for(var i=0;i<window.popsOption.length;i++){
        		if(window.popsOption[i].id == id){
        			return i;
        		}
        	}
        	return -1;
        },
       
        /**
         * 获取当前弹出窗中最上面的弹窗配置信息
         * 提示：该方法不依赖调用对象
         */
        _maxZIndexOptionIndex: function(){
			var index = -1;
        	if(window.popsOption != undefined && window.popsOption.length > 0){
				var maxZIndex = -1;
				for(var i=0;i<window.popsOption.length;i++){
					var thisOptions = window.popsOption[i];
					var thisZIndex = thisOptions.zindex+thisOptions.zindexAdd;
					if(thisZIndex > maxZIndex){
						maxZIndex = thisZIndex;
						index = i;
					}
				}
//				if(index != -1 && window.popsOption[index].keyEsc){
//					var popId = window.popsOption[index].id;
//					$("#simple-modal-"+popId).hideModal();
//				}
			}
			return index;
        },
        /**
         * esc按键的事件,按下esc键后如果配置了keyEsc=true,就会隐藏最上层的弹窗
         * 提示：该方法不依赖调用对象,可使用$.fn._escape()调用
         */
        _escape: function(e) {
        	if(e.keyCode == 27){
        		var index = $.fn._maxZIndexOptionIndex();
				if(index != -1 && window.popsOption[index].keyEsc){
					var popId = window.popsOption[index].id;
					$("#simple-modal-"+popId).hideModal();
				}
			}
        },
		/**
		 * 刷新或者显示当前调用的弹窗（一般是最上层的弹窗，这种调用是通过jQuery对象调用的方式）或者最上层的弹窗
		 * 提示：该方法不依赖调用对象,可使用$.fn._display()调用
		 */
        _display: function() {
        	var thisShowPopOptions = this.options;
        	if(undefined == thisShowPopOptions){//当前调用的jQuery对象不包含弹窗配置参数，则使用弹窗中最上层的弹窗对象的配置参数
        		var index = $.fn._maxZIndexOptionIndex();
        		if(index != -1 ){
        			thisShowPopOptions = window.popsOption[index];
        		}
        	}
        	if(thisShowPopOptions){
        		// Update overlay
//	            $("#simple-modal-overlay-"+thisShowPopOptions.id).css({width: $(window).width(), height: $(window).height()});
	            // Update position popup
	            var modal = $("#simple-modal-"+thisShowPopOptions.id);
				//var thisHeight = modal.height() == 0?(height || 0):modal.height();
				var top = thisShowPopOptions.offsetTop || ($(window).height() - modal.height())/3;
				var duration = 0;
				if(thisShowPopOptions.animate){
					duration = 400;
				}
	            modal.animate({
					opacity: 1,
	                top: top>0?top:0,
	                left: (($(window).width() - modal.width())/2)
	            }, {
	            	duration: duration,
					//easing: "linear",
					start: function() {
						$(this).stop();//防止上次触发的事件还未完成又触发造成的延迟执行
					}
				});
        	}
        }
    });
    
})(jQuery);
