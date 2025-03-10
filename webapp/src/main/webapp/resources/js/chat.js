// the primeFaces-context is required to access the DOM of the scrollable panels
let pfContext;

function scrollToBottom(scrollComponentWidget) {
	let scrollComponentContainer = document
		.getElementById(scrollComponentWidget.id);
	let scrollComponent = scrollComponentContainer.children[0];
	scrollComponent.scrollTop = scrollComponent.scrollHeight
			- scrollComponent.clientHeight;
}

function scrollToChatBottom() {
	scrollToBottom(pfContext('chatWindowWidget'));
}
function scrollToEventLogAndOnlineInfoBottom() {
	scrollToBottom(pfContext('eventLogwWidget'));
	scrollToBottom(pfContext('onlineInfoWidget'));
}